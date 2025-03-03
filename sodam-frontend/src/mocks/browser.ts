import { setupWorker } from 'msw/browser';

import { articleSummaryHandlers } from "./articleSummary.handlers";// 핸들러 가져오기
import { categoriesHandlers } from "./category.handlers";

export const worker = setupWorker(
    ...articleSummaryHandlers,
    ...categoriesHandlers
);