<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Force.com RESTful Services with Force.com</title>
    <meta name="description" content="Force.com REST API with Swagger">
    <meta name="author" content="Thys Michels">
    <meta name="keywords" content="Salesforce, REST, Swagger">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Styles and icons -->
    <link href='<c:url value="/resources/bootstrap/css/bootstrap.min.css"/>' rel="stylesheet">
    <link href='<c:url value="/resources/bootstrap/css/bootstrap-responsive.min.css" />' rel="stylesheet">
    <link href='<c:url value="/resources/bootstrap/css/prettify.css" />' rel="stylesheet">
    <link href='<c:url value="/resources/bootstrap/css/css.css" />' rel="stylesheet">
    
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

  </head>
  <body data-spy="scroll" data-target=".bs-docs-sidebar">

<div id="wrapper">
<div id="wrapper-inner">
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">Force.com RESTful Services with Swagger</a>
          <div class="nav-collapse collapse">
            <ul id="navlist" class="nav">
              <li id="navindex"><a href="#">Home</a></li>
              <li id="navindex"><a href="/model">Model</a></li>
              <li id="navindex"><a href="/controller">Controller</a></li>
              <li><a href="https://github.com/thysmichels/force.com-swagger-rest-spring-mvc">Source</a></li>
              <li><a href="https://github.com/thysmichels/force.com-swagger-rest-spring-mvc/issues">Issues</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
<div id="content">
	<div class="container">
		<form class="form-horizontal" method="POST" action="/model/generate">
             <fieldset>
             	<div class="control-group">
             		<label>Select sobject to generate model for</label>
             		<select name="sobject">
             			<c:forEach items="${sObject}" var="sObject">    
                  			<option>${sObject['name']}</option>
               			</c:forEach>	
             		</select>
             	</div>
             	<div class="control-group">
             		<label>Select API version</label>
             		<select name="version">
                  			<option>v27.0</option>
                  			<option>v26.0</option>
                  			<option>v25.0</option>
                  			<option>v24.0</option>
                  			<option>v23.0</option>
             		</select>
             	</div>
             	<div class="control-group">
    				<label>Enter package name:</label>
		        		<input id="packageName" name="packageName" value="" placeholder="Type package name" required/>
		      	</div>
 				<div class="control-group">
 					<label class="checkbox">
  						Select to Include Custom fields<input type="checkbox" name="customfields">
					</label>
 				</div>
 				      	
                <div class="control-group">
					<div class="controls">
                    	<input type="submit" value="Generate" class="btn btn-primary">
                    </div>
                </div>
              </fieldset>
            </form>
          </div>
</div>
</div>
</div>
</body>
</html>