# Data-analysis-application-for-Yelp.com-s-business-reviews
This application provides a user interface that supports Yelp's business search based on business categories (main and sub-categories) and the attribute associated with each business category
Little bit about Yelp
Yelp was founded in 2004 to help people find great local businesses like dentists, hair stylists and mechanics with a monthly average of 29 million unique visitors who visited Yelp via the Yelp app and 64 million unique visitors who visited Yelp via mobile web. Yelpers have written more than 148 million reviews by the end of Q4 2017. Yelp uses automated software to recommend the most helpful and reliable reviews for the Yelp community among the millions they got. Read More at Yelp

Yelp Dataset Challenge
In 2013, Yelp.com has announced the “Yelp Dataset Challenge” and invited students to use this data in an innovative way and break ground in research. In this project I developed GUI using Java to query this dataset and extract useful information for local businesses and individual users.

Simplified Dataset
Yelp data was available in JSON format. Original Yelp dataset includes 42,153 businesses, 252,898 users, and 1,125,458 reviews from Phoenix (AZ), Las Vegas (NV), Madison (WI) in United States and Waterloo (ON) and Edinburgh (ON) in Canada. Orignial dataset can be downloaded at Yelp dataset download page.

I have used a simplified subset of original dataset as a development dataset. This simplified dataset includes only 20,544 businesses, the reviews that are written for those business only, and the users that wrote those reviews.

Project Scope & Requirements
I designed this project based on assumption that primary users for this application would be potential customers seeking for businesses that match their search criteria. This application have a user interface that supports business search based on business categories (main and sub-categories) and the attribute associated with each business category. I have further used Faceted Search for narrowing down the search for business. Faceted Search has become a popular technique in commercial search applications, particularly for online retailers and libraries. It is a technique for accessing information organized according to a faceted classification system, allowing users to explore a collection of information by applying multiple filters.

In this application, the user can filter the search results using available business attributes (i.e. facets) such a main category(ies), sub-category(ies), business attributes, days of the week, and hours of a day. Each time the user clicks on a facet value; the set of results is reduced to only the items that have that value. Additional clicks continue to narrow down the search—the previous facet values are remembered and applied again.

Project Screenshots
Getting Started
Prerequisites
Following must be installed in order to set up the development environment

Install Oracle Database 11gR2
Install Java 8 JDK
Compiling
As this application depends on two external jar (already included in this repository), compilation of application is divided into two parts:

Setting up database
Run "createDb.sql" script using Oracle SQL Developer to create and setup database tables. Compile "populate.java" to be used for parsing, cleaning and storing dataset into database

$ javac -classpath ./:ojdbc6.jar:json-simple-1.1.jar populate.java
To populate Yelp dataset

$ java -classpath ./:ojdbc6.jar:json-simple-1.1.jar populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json");
Populate.java will populate following tables in database:

Business
Business Hours
Business Main Category
Business Sub Category
Business Attributes
Business Check In
Yelp Users
Yelp Reviews
Compiling and Running the Application
$ javac -classpath ./:ojdbc6.jar:json-simple-1.1.jar GUI.java
Tu run application:

$ java -classpath ./:ojdc6.jar:json-simple-1.1.jar GUI
License
MIT License

Copyright (c) 2018 Harsha Mehra 

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

