import {Link} from "react-router-dom";

const subscriptions = [
    {
        subscriptionId: 1,
        subscriptionImage: "https://images.unsplash.com/photo-1565718253569-3156836e2ec0?q=80&w=3024&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "BRONZE",
        summary: "BRONZE 구독권입니다. 구독자 전용 게시글을 총 50번 읽을 수 있고 최대 15번까지 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
    },
    {
        subscriptionId: 2,
        subscriptionImage: "https://images.unsplash.com/photo-1578670957056-d47e6e5d70bf?q=80&w=2488&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "SILVER",
        summary: "SILVER 구독권입니다. 구독자 전용 게시글을 총 100번 읽을 수 있고 최대 30번까지 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
    },
    {
        subscriptionId: 3,
        subscriptionImage: "https://images.unsplash.com/photo-1578670812003-60745e2c2ea9?w=1400&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjV8fCVFQyVCOSVCNCVFQiU5MyU5Q3xlbnwwfHwwfHx8Mg%3D%3D",
        title: "GOLD",
        summary: "GOLD 구독권입니다. 구독자 전용 게시글을 무제한으로 읽을 수 있고 무제한으로 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
    },
    {
        subscriptionId: 4,
        subscriptionImage: "https://images.unsplash.com/photo-1578670957056-d47e6e5d70bf?q=80&w=2488&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "SILVER",
        summary: "SILVER 구독권입니다. 구독자 전용 게시글을 총 100번 읽을 수 있고 최대 30번까지 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
    },
    {
        subscriptionId: 5,
        subscriptionImage: "https://images.unsplash.com/photo-1578670812003-60745e2c2ea9?w=1400&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjV8fCVFQyVCOSVCNCVFQiU5MyU5Q3xlbnwwfHwwfHx8Mg%3D%3D",
        title: "GOLD",
        summary: "GOLD 구독권입니다. 구독자 전용 게시글을 무제한으로 읽을 수 있고 무제한으로 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
    },
]

export default function Subscriptions() {
    return (
        <div className="subscription__list">
            {
                subscriptions?.length > 0 ? (
                    subscriptions?.map((subscription) => (
                        <div className="subscription__box">
                            <Link to={`/subscription/order/${subscription?.subscriptionId}`}>
                                <img src={subscription.subscriptionImage} alt="구독권 이미지" />
                                <div className="subscription__content">
                                    <div className="subscription__title">{subscription.title}</div>
                                    <div className="subscription__summary">{subscription.summary}</div>
                                </div>
                                <div className="subscription__info">
                                    <div className="subscription__price">원가 : {subscription.cost} 원</div>
                                    <div className="subscription__discount">할인가 : {subscription.discCost} 원</div>
                                    <div className="subscription__sale">구매가 : {subscription.saleCost} 원</div>
                                    <button
                                        className="subscription__buy-btn"
                                    >
                                        구매
                                    </button>
                                </div>
                            </Link>
                        </div>
                    ))
                ) : (
                    <div>판매중인 구독권이 없습니다.</div>
                )
            }
        </div>
    )
}