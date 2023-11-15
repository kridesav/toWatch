
searchInput.addEventListener('input', function () {

    if (searchInput.value === '') {
        suggestionList.style.display = 'none';
        return;
    }

    $.ajax({
        url: '/search/suggestions',
        method: 'GET',
        data: {
            query: searchInput.value
        },
        success: function (data) {

            suggestionList.innerHTML = '';


            for (let i = 0; i < data.length; i++) {
                var suggestion = document.createElement('a');
                suggestion.textContent = data[i].title;
                var listItem = document.createElement('li');
                listItem.appendChild(suggestion);
                listItem.style.textAlign = 'center';
                listItem.style.cursor = 'pointer';
                listItem.onclick = function () {
                    window.location = '/media/' + data[i].type + '/' + data[i].id;
                };
                suggestionList.appendChild(listItem);
            }


            if (data.length > 0) {
                var showAll = document.createElement('a');
                showAll.textContent = 'Show all';
                var listItem = document.createElement('li');
                listItem.appendChild(showAll);
                listItem.style.textAlign = 'center';
                listItem.style.cursor = 'pointer';
                listItem.onclick = function () {
                    window.location = '/search?q=' + encodeURIComponent(searchInput.value);
                };
                suggestionList.appendChild(listItem);


                suggestionList.style.display = 'block';
            } else {

                suggestionList.style.display = 'none';
            }
        }
    });
});

document.addEventListener('click', function (event) {

    var searchContainer = document.querySelector('.d-flex');
    if (!searchContainer.contains(event.target)) {
        suggestionList.style.display = 'none';
        searchInput.value = '';
    }
});

searchInput.addEventListener('focus', function () {

    if (searchInput.value !== '') {
        suggestionList.style.display = 'block';
    }
});

searchInput.addEventListener('keydown', function (event) {

    if (event.key === 'Enter') {

        event.preventDefault();


        window.location = '/search?q=' + encodeURIComponent(searchInput.value);
    }
});

['movie', 'tvshow'].forEach(type => {
    document.querySelectorAll(`.${type}-poster`).forEach(function (img) {
        var mediaId = img.id.replace(`${type}-poster-`, '');
        var mediaTitle = document.querySelector(`a[href="/media/${type}/` + mediaId + '"]').textContent;
        var formattedTitle = mediaTitle.replace(/ /g, '-');
        fetch(`https://www.omdbapi.com/?apikey=f783f5dc&t=${formattedTitle}`)
            .then(response => response.json())
            .then(data => {
                if (data.Poster) {
                    img.src = data.Poster;
                }
            });
    });
});