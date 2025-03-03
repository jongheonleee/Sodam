
// 공통 리스폰스 타입
export interface SodamResponse<T> {
    code: number; // 2xx, 3xx, 4xx 등등
    result: string; // SUCCESS, FAILURE
    message?: string;
    data: T; // 실제 데이터
}
