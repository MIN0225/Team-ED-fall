require("dotenv").config();

const mysql = require("mysql2/promise");
const axios = require("axios");
const cheerio = require("cheerio");
const vm = require("vm");

async function getRoomData(id) {
  const url = `https://m.booking.naver.com/booking/10/bizes/${id}`;
  const response = await axios.get(url);

  const $ = cheerio.load(response.data);
  // 데이터의 위치 : 두 번째 스크립트 태그
  const script = $("script").eq(1).html();

  // 스크립트 실행을 위한 컨텍스트 생성
  const context = {
    window: {}, // node.js 환경에는 window 객체가 없으므로 mock 생성
  };
  vm.createContext(context);

  // 스크립트를 실행하면 window.__APOLLO_STATE__ 객체에 데이터가 담김
  vm.runInContext(script, context);

  // 룸 데이터는 'BizItem'을 포함하는 키의 값으로 존재
  const keys = Object.keys(context.window.__APOLLO_STATE__).filter((key) =>
    key.includes("BizItem")
  );
  const bizItems = keys.map((key) => context.window.__APOLLO_STATE__[key]);
  return bizItems.map(({ id, name, price }) => ({ id, name, price }));
}

// getRoomData(329314).then(console.log)

const dbConfig = {
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_NAME,
};

async function createRoomTables() {
  // MySQL 데이터베이스 연결 설정
  const connection = await mysql.createConnection(dbConfig);

  try {
    // ID 값을 가져오는 쿼리
    const [rows] = await connection.execute(
      "SELECT bookingBusinessId FROM pr_hasbooking"
    );

    await connection.execute(`
    CREATE TABLE IF NOT EXISTS room_datas(
      pr_id INT NOT NULL,
      room_id INT NOT NULL PRIMARY KEY,
      name VARCHAR(255),
      price INT
    )`);

    // 각 합주실에 대해
    for (const row of rows) {

      // 각 룸에 대해
      const data = await getRoomData(row.bookingBusinessId);
      for (const room of data) {
        await connection.execute(
          `INSERT INTO room_datas (pr_id, room_id, name, price) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, price = ?`,
          [
            row.bookingBusinessId,
            room.id,
            room.name,
            room.price,
            room.name,
            room.price,
          ]
        );
      }
    }
  } catch (error) {
    console.error("Error fetching data from the database:", error);
  } finally {
    // 데이터베이스 연결 종료
    if (connection && connection.end) await connection.end();
  }
}

createRoomTables().catch(console.error);
