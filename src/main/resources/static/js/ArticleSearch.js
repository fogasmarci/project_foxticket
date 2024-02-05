document.getElementById("search").addEventListener("submit", function (event) {
    event.preventDefault();
    submitForm(event.target);
});

function submitForm(form) {
    const formData = new FormData(form);
    const searchArticlesRequest = {
        searchKeyword: formData.get("searchKeyword"),
    };

    getRequest(searchArticlesRequest);
}

async function getRequest(searchArticlesRequest) {
    const url = `/api/news?searchKeyword=` + searchArticlesRequest.searchKeyword;

    try {
        const response = await fetch(url);
        if (response.ok) {
            const articleListDTO = await response.json();
            updateTable(articleListDTO);
            updateQuaryString(searchArticlesRequest);
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

function updateQuaryString(searchArticlesRequest) {
    var currentUrl = window.location.href;
    var url = new URL(currentUrl);
    url.searchParams.set("searchKeyword", searchArticlesRequest.searchKeyword);
    window.history.replaceState({}, document.title, url.toString());
}