DELETE FROM TRIVIA_QUESTION;;
DELETE FROM TRIVIA_CATEGORY;;

INSERT INTO TRIVIA_CATEGORY (ID, NAME) values (1, 'Golf champions')
INSERT INTO TRIVIA_CATEGORY (ID, NAME) values (2, 'Golf courses')

INSERT INTO TRIVIA_QUESTION (ID, QUESTION, ANSWER, CATEGORY_ID) VALUES (1, 'What is Tiger Woods'' given first name?', 'Eldrick', 1);;
INSERT INTO TRIVIA_QUESTION (ID, QUESTION, ANSWER, CATEGORY_ID) VALUES (2, 'How many career PGA Tour victories does Jack Nicklaus have?', '73', 1);;
INSERT INTO TRIVIA_QUESTION (ID, QUESTION, ANSWER, CATEGORY_ID) VALUES (3, 'Which holes make up Amen Corner at Augusta?', '11,12,13', 2);;
