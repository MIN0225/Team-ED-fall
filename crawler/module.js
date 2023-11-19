import mysql from "mysql2/promise";
import puppeteer from "puppeteer";
import "dotenv/config";

const dbConfig = {
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_NAME,
};

async function getStartDate(page) {
  const startDate = await page.$eval(
    'td[class*="calendar-selected start-day"]',
    (element) => element.getAttribute("data-cal-datetext")
  );
  console.log("현재 날짜 : ", startDate);
}
async function setCalendar(page, date) {
  let targetDateElement = null;
  while (!targetDateElement) {
    targetDateElement = await page.$(
      `td:not(.calendar-unselectable)[data-cal-datetext="${date}"]`
    );
    if (!targetDateElement) {
      await page.waitForSelector('a[title="다음 달"]');
      await page.evaluate(() => {
        const nextMonthButton = document.querySelector('a[title="다음 달"]');
        nextMonthButton.click();
      });
      await page.waitForSelector(
        'td:not(.calendar-unselectable)'
      );
    }
  }
}

async function getAvailableTime(browser, prId, roomId, date) {
  const page = await browser.newPage();

  // 리소스 로드 제어 - 이미지, 스타일시트, 폰트 등은 로드하지 않음
  await page.setRequestInterception(true);
  page.on("request", (req) => {
    if (["image", "stylesheet", "font"].includes(req.resourceType())) {
      req.abort();
    } else {
      req.continue();
    }
  });
  const url = `https://m.booking.naver.com/booking/10/bizes/${prId}/items/${roomId}`;

  await page.goto(url, {
    waitUntil: "networkidle2",
    timeout: 10000,
  });
  await page.waitForSelector('td:not(.calendar-unselectable)')

  await setCalendar(page, date);

  await page.$eval(
    `td:not(.calendar-unselectable)[data-cal-datetext="${date}"] > a`,
    (element) => element.click()
  );
  await page.waitForTimeout(100);

  const selectedDate = await page.$eval(
    'td[class*="calendar-selected"][class*="start-day"]',
    (element) => element.getAttribute("data-cal-datetext")
  );

  // 예약가능시간 가져오기
  const availableTimes = await page.$$eval(
    'ul[class="lst_time_cont data_"] > li[class="item"] > span[class="time_txt"] > span[ng-bind]',
    (elements) => elements.map((el) => el.textContent)
  );

  // 선택한 날짜와 예약가능시간을 결합하여 datetime 변수 생성
  const datetimeValues = availableTimes.map(
    (time) => `${selectedDate} ${time}`
  );

  await page.close();

  return datetimeValues;
}

function formatDate(date) {
  let day = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();

  // 월과 일이 10보다 작으면 앞에 0 붙이기
  day = day < 10 ? "0" + day : day;
  month = month < 10 ? "0" + month : month;

  return `${year}-${month}-${day}`;
}

function getNextDays(days = 30) {
  let dates = [];
  let currentDate = new Date();

  currentDate.setDate(currentDate.getDate() + 1);

  for (let i = 0; i < days; i++) {
    dates.push(formatDate(currentDate));
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return dates;
}

function delay(time) {
  return new Promise(function (resolve) {
    setTimeout(resolve, time);
  });
}
async function getRoomId() {
  const connection = await mysql.createConnection(dbConfig);
  const [rows] = await connection.execute(
    "SELECT pr_id, room_id FROM room_datas"
  );
  return rows;
}

async function getDataAndInsert(date) {
  let browser = await puppeteer.launch();

  const connection = await mysql.createConnection(dbConfig);

  await connection.execute(`
    CREATE TABLE IF NOT EXISTS reservation_datas(
      room_id INT NOT NULL,
      available_time DATETIME NOT NULL,
      PRIMARY KEY (room_id, available_time)
    )`);
  try {
    const [rows] = await connection.execute(
      "SELECT pr_id, room_id FROM room_datas"
    );
    const reverseRows = rows.reverse();
    // const dates = getNextDays();
    let count = 1;
    for (const row of reverseRows) {
      let attempt = 1;
      while (attempt < 5) {
        try {
          const availableTimes = await getAvailableTime(
            browser,
            prId,
            roomId,
            date
          );
          for (const time of availableTimes) {
            await connection.execute(
              `INSERT INTO reservation_datas (room_id, available_time) VALUES (?, ?) ON DUPLICATE KEY UPDATE available_time = ?`,
              [row.room_id, time, time]
            );
          }
          console.log(
            "Num : ",
            count,
            "roomID : ",
            row.room_id,
            "날짜 : ",
            date
          );
          break; // 성공할 경우 while문 탈출
        } catch {
          console.log(
            "Attempt",
            attempt,
            "failed for room:",
            row.room_id,
            "Date:",
            date
          );
          attempt++;
          await browser.close();
          browser = await puppeteer.launch();
          await delay(10000); // 대기 후 재시도
        }
      }
      await delay(1000); // 다음 요청 전에 대기

      count++;
    }
  } catch (err) {
    console.error("Error:", err);
  } finally {
    await connection.end();
    await browser.close();
  }
}

async function getMonthlyData(prId, roomId) {
  let browser = await puppeteer.launch();
  const connection = await mysql.createConnection(dbConfig);
  const dates = getNextDays();
  let count = 1;
  for (const date of dates) {
    let attempt = 1;
    while (attempt <= 3) {
      try {
        const availableTimes = await getAvailableTime(
          browser,
          prId,
          roomId,
          date
        );
        // 모든 시간을 한 번에 데이터베이스에 삽입
        const queries = availableTimes.map(time => 
          connection.execute(
            `INSERT INTO reservation_datas (room_id, available_time) VALUES (?, ?) ON DUPLICATE KEY UPDATE available_time = ?`,
            [roomId, time, time]
          )
        );
        await Promise.all(queries);
        
        console.log("Num : ", count, "roomID : ", roomId, "날짜 : ", date);
        break; // 성공할 경우 while문 탈출
      } catch(error) {
        console.error(`Attempt ${attempt} failed for room: ${roomId}, Date: ${date}`);
        if (attempt === 3) {
          console.error("Final attempt failed, moving to next date:", error);
        }
        attempt++;
        await delay(10000); // 대기 후 재시도
      }
    }
    await delay(1000); // 다음 요청 전에 대기
    count++;
  }
  await connection.end();
  await browser.close();
}

// 함수 전체를 export
export { getAvailableTime, getDataAndInsert, getMonthlyData, getRoomId, delay };
export default dbConfig;
