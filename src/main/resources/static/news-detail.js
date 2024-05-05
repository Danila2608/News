function getNewsIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('news');
}

function fetchNewsDetail() {
    const newsId = getNewsIdFromUrl();
    fetch(`http://localhost:8080/new/${newsId}`)
        .then(response => response.json())
        .then(news => {
            document.getElementById('news-title').innerText = news.title;
            document.getElementById('news-author').innerHTML = `<span class="bold">Author:</span> ${news.author}`;
            document.getElementById('news-description').innerText = news.description;
            document.getElementById('news-publishedAt').innerHTML = `<span class="bold">Published at:</span> ${new Date(news.publishedAt).toLocaleString()}`;
            document.getElementById('news-content').innerText = news.content;

            const backButton = document.createElement('button');
            backButton.classList.add('back-button');
            backButton.innerText = 'Back';
            backButton.onclick = function() {
                window.location.href = 'index.html';
            };
            document.querySelector('.container').appendChild(backButton);
        })
        .catch(error => console.error('Error:', error));
}

window.onload = fetchNewsDetail;
