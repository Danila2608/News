function fetchNews() {
    fetch('http://localhost:8080/new')
        .then(response => response.json())
        .then(data => {
            const content = document.getElementById('news-content');
            content.innerHTML = '<h2>News</h2>' + data.map(newItem => `
                <div>
                    <h3>${newItem.title}</h3>
                    <p>${newItem.description}</p>
                    <p>Published at: ${new Date(newItem.publishedAt).toLocaleString()}</p>
                    <button onclick="window.location.href='news-detail.html?news=${newItem.id}'">Show full content</button>
                </div>
            `).join('');
        })
        .catch(error => console.error('Error:', error));
}
document.getElementById('add-news-button').addEventListener('click', function() {
    document.getElementById('news-form').classList.toggle('hidden');
});
document.getElementById('news-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const publishedAtInput = document.getElementById('publishedAt').value;
    const publishedAtDate = new Date(publishedAtInput);
    const publishedAtISO = publishedAtDate.toISOString();
    const newsTopicId = document.getElementById('news-topic-id').value;
    const news = {
        author: document.getElementById('author').value,
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        publishedAt: publishedAtISO,
        content: document.getElementById('news-input').value,
    };
    if (newsTopicId) {
        news.topic = {
            id: newsTopicId
        };
    }
    fetch('http://localhost:8080/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(news)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error creating news: ' + response.statusText);
            }
        })
        .then(data => {
            const content = document.getElementById('news-content');
            const newItem = `
                <div>
                    <h3>${data.title}</h3>
                    <p>${data.description}</p>
                    <p>Published at: ${new Date(data.publishedAt).toLocaleString()}</p>
                    <button onclick="window.location.href='news-detail.html?news=${data.id}'">Show full content</button>
                </div>
            `;
            content.innerHTML = '<h2>News</h2>' + newItem + content.innerHTML;
        })
        .catch(error => console.error('Error:', error));
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
