package asia.jhb.yournetwork

import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main()= runBlocking {
    withTimeout(1000){
        while(isActive){
            print("exits")
        }
    }
}