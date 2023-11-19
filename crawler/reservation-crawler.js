import { getMonthlyData } from "./module.js";

process.on('message', async (pairList) => {
    const startTime = Date.now();
    for (let pair of pairList) {
        try {
            await getMonthlyData(pair.pr_id, pair.room_id);
            process.send({ success: true, prId: pair.pr_id, roomId: pair.room_id });
        } catch (error) {
            process.send({ success: false, prId: pair.pr_id, roomId: pair.room_id, error: error.message });
        }
    }
    const endTime = Date.now(); // 작업 종료 시간 기록
    const duration = endTime - startTime; // 소요 시간 계산
    process.send({duration: duration})
    process.exit();
});