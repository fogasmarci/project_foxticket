document.getElementById("refresh").addEventListener("submit", function (event) {
    event.preventDefault();
    getRequest();
});

async function getRequest() {
    const options = {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    };

    try {
        const response = await fetch('/api/news', options);
        if (response.ok) {
            const articles = await response.json();
            updateTable(articles);
        }
    } catch (error) {
        console.error(`${error}`);
    }
}

function updateTable(articles) {
    const table = document.getElementById("articlesTable");

    table.innerHTML = "";

    articles.articles.forEach(article => {
        const row = table.insertRow();
        row.insertCell(0).textContent = article.id;
        row.insertCell(1).textContent = article.title;
        row.insertCell(2).textContent = article.content;
        row.insertCell(3).textContent = article.publishDate;
    });
}