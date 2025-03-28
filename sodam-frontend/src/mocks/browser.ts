import { setupWorker } from 'msw/browser';

import { articleSummaryHandlers } from "./articleSummary.handlers";// 핸들러 가져오기
import { categoriesHandlers } from "./category.handlers";
import { authHandlers } from "./auth.handlers";
import {articleNewHandlers} from "./articleNew.handlers";
import {articleEditHandlers} from "./articleEdit.handlers";
import {profileHandlers} from "./profile.handlers";
import {articleDetailHandlers} from "./articleDetail.handlers";
import {articleLikeHandlers} from "./articleLike.handlers";

export const worker = setupWorker(
    ...articleSummaryHandlers,
    ...categoriesHandlers,
    ...authHandlers,
    ...articleNewHandlers,
    ...articleEditHandlers,
    ...profileHandlers,
    ...articleDetailHandlers,
    ...articleLikeHandlers,
);