<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Employee</title>
	<link rel="stylesheet" href="${path}/webjars/bootstrap/3.3.5/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<strong><span class="glyphicon glyphicon-picture"></span> creditCard</strong>
					</div>
					<div class="panel-body">
						<form:form method="POST" action="/paymentOfCard" class="form-signin form-horizontal" commandName="creditCardDto">
							<c:if test="${message ne null}">
								<div class="alert alert-danger">
									${message}
								</div>
							</c:if>
							<div class="form-group">
								<div class="col-md-12">
									<div class="input-group input-group-md">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-user"></span>
										</span>
										<form:input class="form-control" path="email" placeholder="Enter email" required="true"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div class="input-group input-group-md">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-info-sign"></span>
										</span>
										<form:input class="form-control" path="cardNumber" placeholder="Enter cardNumber" required="true"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div class="input-group input-group-md">
										<span class="input-group-addon">
											<span class="	glyphicon glyphicon-info-sign"></span>
										</span>
										<form:input class="form-control" path="cvv2" placeholder="Enter cvv2" required="true"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div class="input-group input-group-md">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
										<form:input type="date" class="form-control"  path="expiredDate" required="true"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div class="input-group input-group-md">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-usd"></span>
										</span>
										<form:input class="form-control" path="price" placeholder="Enter price" required="true"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-6">
									<img alt="captcha" src="${path}/captcha-servlet">
								</div>
								<div class="col-md-6">
									<form:input class="form-control" path="captcha" placeholder="Enter Captcha" required="true"/>
								</div>
							</div>
							<div class="form-group">
									<button type="submit" class="btn btn-default btn-xs">
										<span class="glyphicon glyphicon-ok-sign"></span> payment
									</button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
