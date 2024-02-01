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
            const articleListDTO = await response.json();
            updateTable(articleListDTO);
        }
    } catch (error) {
        console.error(`${error}`);
    }
}

function updateTable(articleListDTO) {
    const tableData = document.getElementById("articlesTableData");
    tableData.innerHTML = "";

    articleListDTO.articles.forEach(article => {
        const row = tableData.insertRow();
        row.insertCell(0).textContent = article.id;
        row.insertCell(1).textContent = article.title;
        row.insertCell(2).textContent = article.content;
        row.insertCell(3).textContent = article.publishDate;
    });
}