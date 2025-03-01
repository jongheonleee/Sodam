const subscription =     {
        subscriptionId: 4,
        subscriptionImage: "https://images.unsplash.com/photo-1578670957056-d47e6e5d70bf?q=80&w=2488&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "SILVER",
        summary: "SILVER 구독권입니다. 구독자 전용 게시글을 총 100번 읽을 수 있고 최대 30번까지 다운로드할 수 있습니다.",
        discCost: 3000,
        cost: 15000,
        saleCost: 12000
}


export default function SubscriptionOrderForm() {
    return (
        <div className="subscription__form">
            <div className="subscription__box-order">
                <div className="subscription__box-order-info">
                    <img
                        src={subscription?.subscriptionImage}
                        className="subscription__box-image" />
                    <div className="subscription__box-details">
                        <h2>{subscription?.title}</h2>
                        <p>{subscription?.summary}</p>
                    </div>
                </div>

                <hr/>

                <div className="subscription__box-details-pricing">
                    <div>가격 : {subscription?.cost}</div>
                    <div>할인정보 : {subscription?.discCost}</div>
                    <div>최종 주문금액 : {subscription?.saleCost}</div>
                </div>

                <button className="order-button">주문하기</button>
            </div>
        </div>
    )
}