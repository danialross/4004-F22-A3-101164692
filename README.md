# 4004-F22-A3-101164692
 COMP4004 Assignment 3 
 
 all unit test commits are labelled as "test" while acceptance are labelled "A-Test"
 port = localhost:8090
 server must be launched before the acceptance test can be ran
 
 to run acceptance test, run the test from maven test
 running from the IDE causes web driver bug which uses the wrong driver.
 
 if the acceptance test fails, rerun because if player 1 does not have a card to play on the first round 
 the test setup will be offsetted because player 1 would be drawing cards and all asserts will be looking at the wrong place.
 
 
