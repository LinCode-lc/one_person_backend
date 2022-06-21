package work.huangxin.mango.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import work.huangxin.mango.model.MangoUser;
import work.huangxin.mango.model.WXSessionModel;
import work.huangxin.mango.service.MangoUserService;
import work.huangxin.mango.util.common.HttpClientUtil;
import work.huangxin.mango.util.common.JsonUtils;
import work.huangxin.mango.util.login.IsLogin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private MangoUserService mangoUserService;

    /**
     * 登录功能
     *
     * @param code
     * @param mangoUser
     * @return
     */
    @Transactional
    @PostMapping("/Login")
    public IsLogin Login(String code, @RequestBody MangoUser mangoUser) {
        System.out.println(mangoUser);
        System.out.println("0");
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", "wx5236e70c0036864f");
        param.put("secret", "4b3f66f0d0cd9cfddb9afdac4e70b86b");
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        System.out.println(param);
        String wxResult = HttpClientUtil.doGet(url, param);
        System.out.println(wxResult);
        WXSessionModel wxSessionModel = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
        String openid = wxSessionModel.getOpenid();
        mangoUser.setUserOpenid(openid);
        System.out.println(mangoUser);
        return new IsLogin().isTrue(mangoUser, openid, mangoUserService);
   }
    @PostMapping("/checkAdmin")
    public List<MangoUser> checkAdmin(Integer id) {
        MangoUser mangoUser = new MangoUser();
        mangoUser.setUserId(id);
        return mangoUserService.getUserMessageByOtherMessage(mangoUser);
    }
}
