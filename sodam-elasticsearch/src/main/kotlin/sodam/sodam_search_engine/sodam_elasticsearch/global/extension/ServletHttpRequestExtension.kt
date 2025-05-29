package sodam.backend.payment.golbal.extension
import org.springframework.http.server.reactive.ServerHttpRequest


private val mapReqIdToTxId = HashMap<String, String>()

var ServerHttpRequest.txid: String?
    get() {
        return mapReqIdToTxId[id]
    }

    set(value) {
        if (value == null) {
            mapReqIdToTxId.remove(id)
        } else {
            mapReqIdToTxId[id] = value
        }
    }