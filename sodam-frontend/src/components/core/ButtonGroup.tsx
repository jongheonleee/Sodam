import {ReactNode} from "react";

interface ButtonGroupProps {
    align?: string
    children: ReactNode
}

export function ButtonGroup({
    align = "center",
    children
}: ButtonGroupProps) {
    const classList = ['button-group']
    if (align === 'center')
        classList.push('align-center')

    // 추후에 left, right 부분도 고려해야함

    return (
        <div className={classList.join(' ')}>
            { children }
        </div>
    )
}