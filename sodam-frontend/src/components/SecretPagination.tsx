import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

interface SecretePaginationProps {
    page: number
    totalPages: number
    handlePageChange : (event: unknown, value: number) => void
}

export default function SecretPagination({
    page,
    totalPages,
    handlePageChange,
} : SecretePaginationProps) {

    return (
        <Stack className="secret__pagination" spacing={2}>
            <Pagination
                count={totalPages} // totalPages
                page={page} // page
                onChange={handlePageChange}
            />
        </Stack>
    )
}