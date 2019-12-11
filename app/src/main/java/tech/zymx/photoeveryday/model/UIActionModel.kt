package tech.zymx.photoeveryday.model

import tech.zymx.photoeveryday.UIActionType

/**
 * Created by kevinzhan@tencent.com on 2019-12-11
 */
open class UIActionModel(val actionType: UIActionType) {
    open fun getExtraData(): Any {
        return ""
    }

}