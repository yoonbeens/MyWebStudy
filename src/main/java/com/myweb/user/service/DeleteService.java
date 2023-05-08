package com.myweb.user.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myweb.user.model.UserDAO;
import com.myweb.user.model.UserVO;

public class DeleteService implements IUserService {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		

		String id = ((UserVO)request.getSession().getAttribute("user")).getUserId();
		String pw = request.getParameter("check_pw");
		
		UserDAO dao = UserDAO.getInstance();
		
		response.setContentType("text/html; charset=UTF-8");

		int result = dao.userCheck(id, pw);
		String htmlCode;
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			
			if(result == 1) {
				dao.deleteUser(id);
				request.getSession().invalidate();
				htmlCode = "<script>\r\n"
	                    + "alert('정상 탈퇴 처리되었습니다.');\r\n"
	                    + "location.href='/MyWeb';\r\n"
	                    + "</script>";
			} else {
						htmlCode = "<script>\r\n"
	                    + "alert('비밀번호가 일치하지 않습니다.');\r\n"
	                    + "location.href='/MyWeb/myPage.user';\r\n"
	                    + "</script>";
			}
			out.print(htmlCode);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

	}

}
