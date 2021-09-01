package asia.jhb.yournetwork

sealed interface Result {
    val message: Int

    object Success : Result {
        override val message = R.string.login_success
    }

    object InusePC : Result {
        override val message = R.string.inuse_pc
    }

    object InuseMobile : Result {
        override val message = R.string.inuse_mobile
    }

    object InputIncomplete : Result {
        override val message = R.string.input_incomplete
    }

    object Undefined : Result {
        var response = ""
        override val message = R.string.undefined
    }

    object TimeOut : Result {
        override val message = R.string.time_out
    }
}