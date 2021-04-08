<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>GARO ESTATE | Home page</title>
        <meta name="description" content="GARO is a real-estate template">
        <meta name="author" content="Kimarotec">
        <meta name="keyword" content="html5, css, bootstrap, property, real-estate theme , bootstrap template">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,700,800' rel='stylesheet' type='text/css'>

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="icon" href="favicon.ico" type="image/x-icon">

        <link rel="stylesheet" href="resources/assets/css/normalize.css">
        <link rel="stylesheet" href="resources/assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="resources/assets/css/fontello.css">
        <link href="resources/assets/fonts/icon-7-stroke/css/pe-icon-7-stroke.css" rel="stylesheet">
        <link href="resources/assets/fonts/icon-7-stroke/css/helper.css" rel="stylesheet">
        <link href="resources/assets/css/animate.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" href="resources/assets/css/bootstrap-select.min.css"> 
        <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="resources/assets/css/icheck.min_all.css">
        <link rel="stylesheet" href="resources/assets/css/price-range.css">
        <link rel="stylesheet" href="resources/assets/css/owl.carousel.css">  
        <link rel="stylesheet" href="resources/assets/css/owl.theme.css">
        <link rel="stylesheet" href="resources/assets/css/owl.transitions.css">
        <link rel="stylesheet" href="resources/assets/css/style.css">
        <link rel="stylesheet" href="resources/assets/css/responsive.css">
    </head>
    <body>

        <div id="preloader">
            <div id="status">&nbsp;</div>
        </div>
        <!-- Body content -->

        <div class="header-connect">
            <div class="container">
                <div class="row">
                    <div class="col-md-5 col-sm-8  col-xs-12">
                        <div class="header-half header-call">
                            <p>
                                <span><i class="pe-7s-call"></i> +1 234 567 7890</span>
                                <span><i class="pe-7s-mail"></i> your@company.com</span>
                            </p>
                        </div>
                    </div>
                    <div class="col-md-2 col-md-offset-5  col-sm-3 col-sm-offset-1  col-xs-12">
                        <div class="header-half header-social">
                            <ul class="list-inline">
                                <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                                <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                                <li><a href="#"><i class="fa fa-vine"></i></a></li>
                                <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                                <li><a href="#"><i class="fa fa-dribbble"></i></a></li>
                                <li><a href="#"><i class="fa fa-instagram"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>        
        <!--End top header -->

        <nav class="navbar navbar-default ">
            <div class="container">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navigation">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="getMain"><img src="resources/assets/img/logo.png" alt="클릭 시 메인페이지로 이동"></a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse yamm" id="navigation">
                
               <a href="/">홈으로가기</a>
               <c:if test="${empty users}">
                    <div class="button navbar-right">
                        <button class="navbar-btn nav-button wow bounceInRight login" onclick="location.href='login'" data-wow-delay="0.45s">로그인</button>
                        <button class="navbar-btn nav-button wow fadeInRight" onclick="location.href='insertUsers'" data-wow-delay="0.48s">회원가입</button>
                    </div>
               </c:if>    
               <c:if test="${users.auth eq 'ADMIN'}">
               <h5>${sessionScope.users.id}님의 페이지입니다.</h5>
               		 <button class="navbar-btn nav-button wow bounceInRight login" onclick="" data-wow-delay="0.45s">관리자페이지</button>
              	<form action="logout" method="get">
						<input type="submit" value="logout">
				</form>
               </c:if>
               <c:if test="${users.auth eq 'USER'}">
               <h5>${sessionScope.users.id}님의 페이지입니다.</h5>
               	<form action="logout" method="get">
						<input type="submit" value="logout">
				</form>
               </c:if>
                    <ul class="main-nav nav navbar-nav navbar-right">
                        <li class="dropdown ymm-sw " data-wow-delay="0.1s">
                            <a href="index.html" class="dropdown-toggle active" data-toggle="dropdown" data-hover="dropdown" data-delay="200">채용공고 <b class="caret"></b></a>
                            <ul class="dropdown-menu navbar-nav">
                                <li>
                                    <a href="index-2.html">Home Style 2</a>
                                </li>
                                <li>
                                    <a href="index-3.html">Home Style 3</a>
                                </li>
                                <li>
                                    <a href="index-4.html">Home Style 4</a>
                                </li>
                                <li>
                                    <a href="index-5.html">Home Style 5</a>
                                </li>

                            </ul>
                        </li>


                        <li class="wow fadeInDown" data-wow-delay="0.2s"><a class="" href="job">채용공고</a></li>
                        <li class="wow fadeInDown" data-wow-delay="0.3s"><a class="" href="MentorList">멘토리스트</a></li>
                        <li class="wow fadeInDown" data-wow-delay="0.3s"><a class="" href="serviceCenter">커뮤니티</a></li>
                        <li class="dropdown yamm-fw" data-wow-delay="0.4s">
                            <a href="serviceCenterQna" class="dropdown-toggle active" data-toggle="dropdown" data-hover="dropdown" data-delay="200">고객센터<b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <div class="yamm-content">
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h5><a href="inquire">문의</a></h5>
                                                <ul>
                                                    <li>
                                                        <a href="inquire">문의하기</a>
                                                    </li>
                                                    <li>
                                                        <a href="inquireList">문의 목록</a>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="col-sm-3">
                                                <h5><a href="serviceCenterQna">결제/환불</a></h5>
                                                <ul>
                                                	<li>
                                                        <a href="serviceCenterQna">질문 목록</a>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="col-sm-3">
                                                <h5><a href="serviceCenterQna2">신청/취소</a></h5>
                                                <ul>
                                                	<li>
                                                        <a href="serviceCenterQna2">질문 목록</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.yamm-content -->
                                </li>
                            </ul>
                        </li>
                        
                        <c:if test="${not empty users }">
							<li class="wow fadeInDown" data-wow-delay="0.5s"><a href="logout">로그아웃</a></li>
                        </c:if>
                        <li class="wow fadeInDown" data-wow-delay="0.5s"><a href="mypageHome">마이페이지</a></li>
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
        <!-- End of nav bar -->
    </body>
</html>