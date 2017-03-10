package webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import webapp.services.TweetsService;

@Controller
public class DefaultController {
  private final TweetsService tweetsService;
  private static final int RESULTS_ON_PAGE = 5;

  @Autowired
  public DefaultController(TweetsService tweetsService) {
    this.tweetsService = tweetsService;
  }

  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("tweets", tweetsService.getTweetsLimitedBy(RESULTS_ON_PAGE));

    return "main";
  }

  @RequestMapping("/search")
  public String search(Model model, @RequestParam("id") String id, @RequestParam("msg") String msg) {
    if (id.isEmpty() && msg.isEmpty()) {
      model.addAttribute("tweets", tweetsService.getTweetsLimitedBy(RESULTS_ON_PAGE));
    } else if (!msg.isEmpty()) {
      model.addAttribute("tweets", tweetsService.findTweetsByMsg(msg, RESULTS_ON_PAGE));
    } else {
      model.addAttribute("tweets", tweetsService.findTweetsById(id));
    }

    return "main";
  }
}
