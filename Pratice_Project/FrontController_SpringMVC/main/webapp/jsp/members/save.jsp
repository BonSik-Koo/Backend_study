<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>

<%
       //java코드를 작성할수 있다
       //jsp도 결국 서블릿으로 자동으로 바뀌어 사용되기 때문에 서블릿의 request, response를 객체를 그냥 사용이 가능하다.!!!!!
       // jsp를 사용해서 동적으로 html을 만들수 있다.(원하는값으로)

        MemberRepository memberRepository = MemberRepository.getInstance();

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);
%>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
성공
<ul>
 <li>id=<%=member.getId()%></li>
 <li>username=<%=member.getUsername()%></li>
 <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>

