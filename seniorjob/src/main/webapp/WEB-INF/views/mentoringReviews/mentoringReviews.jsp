<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<jsp:include page="../topHeader.jsp"></jsp:include>
<body>

	<!-- property area -->
	<div class="properties-area recent-property"
		style="background-color: #FFF;">
		<div class="container">
			<div class="row">

				<div class="col-md-3 p0 padding-top-40">
					<div class="blog-asside-right pr0">
						<div class="panel panel-default sidebar-menu wow fadeInRight animated">
							<div class="panel-heading">
								<h3 class="panel-title">커뮤니티</h3>
							</div>
						</div>
						<hr>
						<div class="panel panel-default sidebar-menu wow fadeInRight animated">
							<div class="panel-heading" onclick="location.href='serviceCenter'">
								<h3 class="panel-title">공지사항</h3>
							</div>
						</div>
						<div class="panel panel-default sidebar-menu wow fadeInRight animated">
							<div class="panel-heading">
								<h3 class="panel-title">멘토링 후기</h3>
							</div>
						</div>
						<div class="panel panel-default sidebar-menu wow fadeInRight animated">
							<div class="panel-heading" onclick="location.href='seniorTimes'">
								<h3 class="panel-title">시니어 타임즈</h3>
							</div>
						</div>
					</div>
				</div>
			<div>
					<h4 class="s-property-title">최대 인기 조회수 글입니다!</h4>
					<hr>
					<div align="center">
						<ul>
						<li><span class="col-xs-6 col-sm-4 col-md-1 add-d-title">글번호</span>
							<span class="col-xs-6 col-sm-4 col-md-1 add-d-title">제목</span>
							<span class="col-xs-6 col-sm-4 col-md-1 add-d-title">조회수</span>
						</li>
						<c:forEach items="${list}" var="mini">
							<span class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${mini.seq }</span>
							<span class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${mini.title }</span>
							<span class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${mini.click }</span>
						</c:forEach>
						</ul>	
					</div>
					</div><br>		
			<div>		
				<div class="section additional-details">

					<h4 class="s-property-title">멘토링 후기</h4>
					
					<!-- 검색 -->
					<div class="search_help" align="right">
			        <form action="/zf_user/help/help-word/help-search">
			            <label for="helpSearchInput" class="copy">
			            </label>
			            <select>
							<option value="">제목</option>
							<option value="">내용</option>
							<option value="">전체</option>
						</select>
<!-- 						<div align="right"> -->
<!-- 			            	<input id="helpSearchInput" name="keyword" class="input_search_help" type="text" placeholder="특수문자는 제외 하고 입력해주세요" value=""> -->
<!-- 			            </div> -->
			            <button class="btn_search_help" type="submit">
			                <span class="blind">검색하기</span>
			            </button>
			        </form>
<!--   				  </div> -->
					<!-- 검색 -->
<!-- 					<div class="col-sm-3" align="right"> -->
<!-- 						<select> -->
<!-- 							<option value="">공지</option> -->
<!-- 							<option value="">이벤트</option> -->
<!-- 						</select> -->
<!-- 						<button type="button">검색하기</button> -->
<!--                         <input type="text" class="form-control" id="find" value="검색할 내용을 입력하세요."> -->
<!--                     </div> -->
				</div>
					
					<ul>
						<li><span class="col-xs-6 col-sm-4 col-md-1 add-d-title">글번호</span>
							<span class="col-xs-6 col-sm-4 col-md-3 add-d-title">제목</span>
							<span class="col-xs-6 col-sm-4 col-md-1 add-d-title">작성자</span>
							<span class="col-xs-6 col-sm-4 col-md-2 add-d-title">작성일자</span>
							<span class="col-xs-6 col-sm-4 col-md-1 add-d-title">조회수</span>
						</li>
						<c:forEach items="${list }" var="rev">
							<span onclick="location.href='getSearchMenReview?seq=${rev.seq }'" class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${rev.seq }</span>
							<span onclick="location.href='getSearchMenReview?seq=${rev.seq }'" class="col-xs-6 col-sm-8 col-md-3 add-d-entry">${rev.title }</span>
							<span onclick="location.href='getSearchMenReview?seq=${rev.seq }'" class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${rev.id }</span>
							<span onclick="location.href='getSearchMenReview?seq=${rev.seq }'" class="col-xs-6 col-sm-8 col-md-2 add-d-entry"><fmt:formatDate value="${rev.w_date }" pattern="yyyy-MM-dd"/></span>
							<span onclick="location.href='getSearchMenReview?seq=${rev.seq }'" class="col-xs-6 col-sm-8 col-md-1 add-d-entry">${rev.click }</span>
						<br>
						</c:forEach>	
					</ul>
			</div>	
					
					<hr>
					
			</div>
		</div>
</div>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>