CREATE TABLE Topic (
                       id SERIAL PRIMARY KEY,
                       category VARCHAR(255) NOT NULL,
                       popularity INT NOT NULL
);

CREATE TABLE News (
                      id SERIAL PRIMARY KEY,
                      author VARCHAR(255),
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      url VARCHAR(255),
                      urlToImage VARCHAR(255),
                      publishedAt TIMESTAMP,
                      content TEXT,
                      topic_id INT REFERENCES Topic(id)
);