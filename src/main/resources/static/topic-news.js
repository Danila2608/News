function getTopicIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('topic');
}

function fetchNewsByTopic() {
    const topicId = getTopicIdFromUrl();
    fetch(`http://localhost:8080/topics/${topicId}`)
        .then(response => response.json())
        .then(topic => {
            document.getElementById('topic-title').innerText = topic.category;
            const content = document.getElementById('news-content');
            content.innerHTML = topic.newsList.map(news => `
                <div>
                    <h3>${news.title}</h3>
                    <p><span class="bold-text">Author:</span> ${news.author}</p>
                    <p>${news.description}</p>
                    <p><span class="bold-text">Published at:</span> ${new Date(news.publishedAt).toLocaleString()}</p>
                    <div class="button-container">
                        <button class="show-content-button" onclick="window.location.href='news-detail.html?news=${news.id}'">Show full content</button>
                    </div>
                </div>
            `).join('');
        })
        .catch(error => console.error('Error:', error));
}

window.onload = fetchNewsByTopic;
