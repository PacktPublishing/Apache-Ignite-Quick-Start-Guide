package com.datagrid.controller;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datagrid.dto.Player;

@Controller
@RequestMapping("/")
public class PlayerController {
	AtomicLong idGenerator = new AtomicLong(100);

	@PostMapping("/player")
	public String createPlayer(HttpSession session, Model model, Player newPlayer) {
		Long playerId = idGenerator.incrementAndGet();
		newPlayer.setId(playerId);
		session.setAttribute(playerId.toString(), newPlayer);
		model.addAttribute("model", String.format("Player ID= %s added", playerId));
		return "player";
	}
	
	
	@GetMapping(value = "/")
    public String showForm(Model model, HttpServletRequest request) {
		request.getSession(true);
		model.addAttribute("player", new Player());
        return "index";
    }
	
	@GetMapping(value = "/player")
    public String showDetails(@RequestParam("id") String playerId, Model model,HttpSession session ) {
		  Object player = session.getAttribute(playerId);
		  String response = "[@%s] %s";
		  if(player == null) {
			  response = String.format(response, new Date(), "<font color='red'>Player Not found</font>");
		  }else {
			  response = String.format(response, new Date(), player);
		  }
		model.addAttribute("model", response);
        return "player";
    }
}
