import {ReactNode} from "react";
import "./Button.css"

interface ButtonProps {
    size?: "small" | "medium" | "large"
    type?: "button" | "submit" | "reset"
    onClick?: () => void
    children: ReactNode
    className?: string
    isDisabled?: boolean
}

export function Button({
    size = "small",
    type = "button",
    onClick,
    children,
    className,
    isDisabled = false
}: ButtonProps) {
    // 클래스명 동적으로 생성
    const classList = ["button"]
    classList.push(size)
    if (className && className.length >= 1) { // 전달받은 클래스명이 존재할 경우
        classList.push(className)
    }

    return (
        <button
            type={type}
            className={classList.join(' ')}
            onClick={onClick}
            disabled={isDisabled}
        >
            {children}
        </button>
    )
}