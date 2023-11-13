// Add an input event listener
searchInput.addEventListener('input', function () {
    // If the input is empty, hide the suggestions list
    if (searchInput.value === '') {
        suggestionList.style.display = 'none';
        return;
    }
    // Send an AJAX request to the server
    $.ajax({
        url: '/search/suggestions',
        method: 'GET',
        data: {
            query: searchInput.value
        },
        success: function (data) {
            // Clear the current suggestions
            suggestionList.innerHTML = '';

            // Add the new suggestions
            for (let i = 0; i < data.length; i++) {
                var suggestion = document.createElement('a');
                suggestion.textContent = data[i].title;
                var listItem = document.createElement('li');
                listItem.appendChild(suggestion);
                listItem.style.textAlign = 'center';
                listItem.style.cursor = 'pointer'; // Change the cursor to a pointer when hovering over the list item
                listItem.onclick = function () { // Add a click event to the list item
                    window.location = '/media/' + data[i].type + '/' + data[i].id;
                };
                suggestionList.appendChild(listItem);
            }

            // Add the "Show all" option
            if (data.length > 0) {
                var showAll = document.createElement('a');
                showAll.textContent = 'Show all';
                var listItem = document.createElement('li');
                listItem.appendChild(showAll);
                listItem.style.textAlign = 'center';
                listItem.style.cursor = 'pointer'; // Change the cursor to a pointer when hovering over the list item
                listItem.onclick = function () { // Add a click event to the list item
                    window.location = '/search?q=' + encodeURIComponent(searchInput.value);
                };
                suggestionList.appendChild(listItem);

                // Show the suggestions list
                suggestionList.style.display = 'block';
            } else {
                // Hide the suggestions list
                suggestionList.style.display = 'none';
            }
        }
    });
});

document.addEventListener('click', function (event) {
    // If the click was outside the search container, hide the suggestions list
    var searchContainer = document.querySelector('.d-flex');
    if (!searchContainer.contains(event.target)) {
        suggestionList.style.display = 'none';
        searchInput.value = '';
    }
});

searchInput.addEventListener('focus', function () {
    // If the input is not empty, show the suggestions list
    if (searchInput.value !== '') {
        suggestionList.style.display = 'block';
    }
});

searchInput.addEventListener('keydown', function (event) {
    // If the Enter key is pressed
    if (event.key === 'Enter') {
        // Prevent the form from being submitted
        event.preventDefault();

        // Redirect to the /search page with the current search query
        window.location = '/search?q=' + encodeURIComponent(searchInput.value);
    }
});