import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

interface ArticlePaginationProps {
    page : number,
    totalPages : number,
    handlePageChange : (event: unknown, value: number) => void,
}

export default function ArticlePagination({
    page,
    totalPages,
    handlePageChange,
}: ArticlePaginationProps) {


    return (
        <Stack className="article__pagination" spacing={2}>
            <Pagination
                count={totalPages} // totalPages
                page={page} // page
                onChange={handlePageChange}
            />
        </Stack>
    )
}