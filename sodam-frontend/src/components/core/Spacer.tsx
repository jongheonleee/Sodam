interface SpacerProps {
    size: number
}

export function Spacer({
    size = 1
}: SpacerProps) {
    return <div style={{ height: `${size}rem` }} />
}