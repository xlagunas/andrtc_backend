package cat.xlagunas.andrtc.token

class ExistingTokenException(throwable: Throwable) : Throwable("This token already exist", throwable)
