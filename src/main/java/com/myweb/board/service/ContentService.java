package com.myweb.board.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myweb.board.model.BoardDAO;
import com.myweb.board.model.BoardVO;

public class ContentService implements IBoardService {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		int bId = Integer.parseInt(request.getParameter("bId"));
		BoardDAO dao = BoardDAO.getInstance();
		
		/*
        # 쿠키로 조회수를 컨트롤 해 보자.
        1. 사용자가 글 제목을 눌러서 상세보기 요청을 보낼 때 
        글 번호로 된 쿠키를 하나 만들어 줄 겁니다. (String)
        쿠키 이름과 쿠키에 저장할 값을 모두 글 번호로 만들어 주겠습니다.
        쿠키의 수명은 15초로 설정하겠습니다.
        
        2. 요청을 보낼 때 같이 넘어온 쿠키 중에, 
         현재 글 번호와 일치하는 쿠키가 존재한다면 조회수를 올려주지 않을 겁니다.
         현재 글 번호와 일치하는 쿠키가 없다면 조회수를 올려주도록 하겠습니다.  
        */
		
		String cId = request.getParameter("bId");
		Cookie cookies[] = request.getCookies();		
		boolean flag = false;
		
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals(cId)) {
					flag = true;
					break;
				}
			}
			if(flag==false) {
				Cookie hitCheck = new Cookie(cId, cId);
				hitCheck.setMaxAge(15);
				response.addCookie(hitCheck);
				dao.upHit(bId);				
			}
			
		}

		BoardVO vo = dao.contentBoard(bId);
		
		//textarea의 줄 개행 기본값인 \r\n을 p태그의 기본값 <br>로 바꿔주기
		vo.setContent(vo.getContent().replace("\r\n", "<br>"));
		request.setAttribute("content", vo);
		
		
	}

}
