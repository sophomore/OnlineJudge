package kr.jadekim.oj.mainserver.controller.WebController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.jadekim.oj.mainserver.entity.Answer;
import kr.jadekim.oj.mainserver.entity.CurrentUser;
import kr.jadekim.oj.mainserver.entity.User;
import kr.jadekim.oj.mainserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ohyongtaek on 2016. 3. 4..
 */

@Controller
public class WebUtilController {

    Gson gson = new GsonBuilder().create();

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping("/myPage")
    public ModelAndView mypage(ModelAndView modelAndView, Authentication authentication){
        ArrayList<Map> message = new ArrayList<>();
        CurrentUser currentUser= (CurrentUser) authentication.getPrincipal();
        User loginUser = currentUser.getUser();
        if(loginUser == null){
            modelAndView.setViewName("redirect:/notice");
            return modelAndView;
        }
        ArrayList<Integer> solvedProblemnum = new ArrayList<>();
        ArrayList<Integer> unsolvedProblemnum = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if(loginUser.getAnswers() == null){
            map.put("solvedProblemnum", null);
            map.put("unsolvedProblemnum", null);
            map.put("user_name", loginUser.getName());
            map.put("user_id", loginUser.getLoginId());
            map.put("solvedProblem", 0);
            map.put("submit", 0);
            if(loginUser.getGroup() == null){
                map.put("group", null);
            }else {
                map.put("group", loginUser.getGroup().getName());
            }
            map.put("correct", 0);
            map.put("incorrect", 0);
        }
        for(Answer answer : loginUser.getAnswers()){
            int temp_ans = answer.getId();
            boolean check = true;
            if(answer.getResult().getIsSuccess() == true){
                for(int i=0;i<solvedProblemnum.size();i++){
                    if(temp_ans == solvedProblemnum.get(i)){
                        check = false;
                    }
                }
                if(check == true){
                    solvedProblemnum.add(temp_ans);
                }
            }else{
                for(int i=0;i<unsolvedProblemnum.size();i++){
                    if(temp_ans == unsolvedProblemnum.get(i)){
                        check = false;
                    }
                }
                if(check == true){
                    unsolvedProblemnum.add(temp_ans);
                }
            }
        }
        map.put("solvedProblemnum", solvedProblemnum);
        map.put("unsolvedProblemnum", unsolvedProblemnum);
        map.put("solvedProblem", solvedProblemnum.size());
        map.put("submit", solvedProblemnum.size()+unsolvedProblemnum.size());
        if(loginUser.getGroup() == null){
            map.put("group", null);
        }else {
            map.put("group", loginUser.getGroup().getName());
        }
        int count = 0;
        for(Answer answer : loginUser.getAnswers()){
            if(answer.getResult().getIsSuccess()){
                count++;
            }
        }
        map.put("correct", count);
        map.put("incorrect", loginUser.getAnswers().size() - count);
        map.put("user_name", loginUser.getName());
        map.put("user_id", loginUser.getloginId());

        modelAndView.addObject("messages", map);
        modelAndView.setViewName("mypage");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/myPage/setting", method = RequestMethod.GET)
    public ModelAndView showSetting(ModelAndView modelAndView, Authentication authentication) {
        CurrentUser currentUser= (CurrentUser) authentication.getPrincipal();
        User loginUser = currentUser.getUser();
        if (loginUser == null) {
            modelAndView.setViewName("rediret:/notice");
            return modelAndView;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", loginUser.getName());
            map.put("email", loginUser.getEmail());
            modelAndView.addObject("messages", map);
            modelAndView.setViewName("settinglist");
            return modelAndView;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/myPage/setting", method = RequestMethod.POST)
    public ModelAndView modifyinfo(ModelAndView modelAndView, HttpServletRequest request, Authentication authentication){
        CurrentUser currentUser= (CurrentUser) authentication.getPrincipal();
        User loginUser = currentUser.getUser();
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        loginUser.setName(name);
        loginUser.setEmail(email);
        modelAndView.setViewName("redirect:/myPage/setting");
        return modelAndView;
    }
}
