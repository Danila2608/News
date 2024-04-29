function fetchNews() {
    fetch('http://localhost:8080/new')
        .then(response => response.json())
        .then(data => {
            const content = document.getElementById('news-content');
            content.innerHTML = '<h2>News</h2>' + data.map(newItem => `
                <div>
                    <h3>${newItem.title}</h3>
                    <p>Author: ${newItem.author}</p>
                    <p>${newItem.description}</p>
                    <p>Published at: ${new Date(newItem.publishedAt).toLocaleString()}</p>
                    <button onclick="window.location.href='news-detail.html?news=${newItem.id}'">Show full content</button>
                </div>
            `).join('');
        })
        .catch(error => console.error('Error:', error));
}

document.getElementById('fetch-news-button').addEventListener('click', function() {
    const newsContent = document.getElementById('news-content');
    if (newsContent.style.display === 'none' || newsContent.innerHTML === '') {
        fetchNews();
        newsContent.style.display = 'block';
    } else {
        newsContent.style.display = 'none';
    }
});

document.getElementById('add-news-button').addEventListener('click', function() {
    document.getElementById('news-form').classList.toggle('hidden');
});

document.getElementById('delete-news-button').addEventListener('click', function() {
    document.getElementById('delete-news-form').classList.toggle('hidden');
});

document.getElementById('edit-news-button').addEventListener('click', function() {
    document.getElementById('edit-news-form').classList.toggle('hidden');
});

document.getElementById('news-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const publishedAtInput = document.getElementById('publishedAt').value;
    const publishedAtDate = new Date(publishedAtInput);
    const publishedAtISO = publishedAtDate.toISOString();

    const news = {
        author: document.getElementById('author').value,
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        url: document.getElementById('url').value,
        urlToImage: document.getElementById('urlToImage').value,
        publishedAt: publishedAtISO,
        content: document.getElementById('news-input').value,
        topic: {
            id: document.getElementById('news-topic-id').value
        }
    };

    fetch('http://localhost:8080/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(news)
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
});

document.getElementById('delete-news-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const newsId = document.getElementById('news-id').value;

    fetch(`http://localhost:8080/new/${newsId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                console.log(`News with ID ${newsId} has been deleted.`);
            } else {
                console.error('Error:', response.status);
            }
        })
        .catch(error => console.error('Error:', error));
});

function editNews(newsId, news) {
    fetch(`http://localhost:8080/new/${newsId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(news)
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
}

document.getElementById('edit-news-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const newsId = document.getElementById('edit-news-id').value;

    const publishedAtInput = document.getElementById('edit-publishedAt').value;
    const publishedAtDate = new Date(publishedAtInput);
    const publishedAtISO = publishedAtDate.toISOString();

    const news = {
        author: document.getElementById('edit-author').value,
        title: document.getElementById('edit-title').value,
        description: document.getElementById('edit-description').value,
        url: document.getElementById('edit-url').value,
        urlToImage: document.getElementById('edit-urlToImage').value,
        publishedAt: publishedAtISO,
        content: document.getElementById('edit-news-input').value
    };

    fetch(`http://localhost:8080/new/${newsId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(news)
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
});

function fetchNewsById(id) {
    fetch(`http://localhost:8080/new/${id}`)
        .then(response => response.json())
        .then(news => {
            document.getElementById('edit-author').value = news.author;
            document.getElementById('edit-title').value = news.title;
            document.getElementById('edit-description').value = news.description;
            document.getElementById('edit-url').value = news.url;
            document.getElementById('edit-urlToImage').value = news.urlToImage;
            document.getElementById('edit-publishedAt').value = news.publishedAt;
            document.getElementById('edit-news-input').value = news.content;
        })
        .catch(error => console.error('Error:', error));
}

document.getElementById('edit-news-id').addEventListener('change', function(event) {
    const newsId = event.target.value;
    fetchNewsById(newsId);
});

document.getElementById('fetch-topics-button').addEventListener('click', function() {
    const topicsContent = document.getElementById('topics-content');
    if (topicsContent.style.display === 'none' || topicsContent.innerHTML === '') {
        fetchAndDisplayTopics();
        topicsContent.style.display = 'block';
    } else {
        topicsContent.style.display = 'none';
    }
});

document.getElementById('add-topic-button').addEventListener('click', function() {
    document.getElementById('topic-form').classList.toggle('hidden');
});

document.getElementById('delete-topic-button').addEventListener('click', function() {
    document.getElementById('delete-topic-form').classList.toggle('hidden');
});

document.getElementById('edit-topic-button').addEventListener('click', function() {
    document.getElementById('edit-topic-form').classList.toggle('hidden');
});

document.getElementById('topic-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const topic = {
        category: document.getElementById('category').value,
        popularity: document.getElementById('popularity').value
    };

    fetch('http://localhost:8080/topics', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(topic)
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
});

document.getElementById('delete-topic-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const topicId = document.getElementById('topic-id').value;

    fetch(`http://localhost:8080/topics/${topicId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                console.log(`Topic with ID ${topicId} has been deleted.`);
            } else {
                console.error('Error:', response.status);
            }
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('edit-topic-id').addEventListener('change', function() {
    const topicId = this.value;

    fetch(`http://localhost:8080/topics/${topicId}`)
        .then(response => response.json())
        .then(topic => {
            document.getElementById('edit-category').value = topic.category;
            document.getElementById('edit-popularity').value = topic.popularity;
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('edit-topic-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const topicId = document.getElementById('edit-topic-id').value;

    const topic = {
        category: document.getElementById('edit-category').value,
        popularity: document.getElementById('edit-popularity').value
    };

    fetch(`http://localhost:8080/topics/${topicId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(topic)
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
});

// Функция для получения и отображения всех тем
function fetchAndDisplayTopics() {
    fetch('http://localhost:8080/topics')
        .then(response => response.json())
        .then(topics => {
            const content = document.getElementById('topics-content');
            content.innerHTML = '<h2>Topics</h2>' + topics.map(topic => `
                <div>
                    <h3><button class="topic-button" onclick="window.location.href='topic-news.html?topic=${topic.id}'">${topic.category}</button></h3>
                    <p>Popularity: ${topic.popularity}</p>
                </div>
            `).join('');
        })
        .catch(error => console.error('Error:', error));
}