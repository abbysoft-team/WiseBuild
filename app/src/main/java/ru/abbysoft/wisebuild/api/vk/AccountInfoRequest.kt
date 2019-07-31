package ru.abbysoft.wisebuild.api.vk

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.abbysoft.wisebuild.model.VkUser

/**
 * Get account info from vk sdk api
 *
 * @author apopov
 */
class AccountInfoRequest : VKRequest<VkUser>("account.getProfileInfo") {

    override fun parse(r: JSONObject): VkUser {
        val accountInfo = r.getJSONObject("response")

        return VkUser.parse(accountInfo)
    }
}