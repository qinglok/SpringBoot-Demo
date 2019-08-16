package xyz.beingx.basespring.api.model

class ResponseToken (
        msg : String? = null,
        var token : String? = null
) : ResponseBase(msg)