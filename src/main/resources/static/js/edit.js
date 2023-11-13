$(document).ready(function () {
    $('.edit-btn').on('click', function () {
        var id = $(this).data('id');
        var type = $(this).data('type');
        var title = $(this).data('title');
        var description = $(this).data('description');
        var genre = $(this).data('genre');
        var releaseYear = $(this).data('releaseyear');
        var director = $(this).data('director');

        $('#editId').val(id);
        $('#editType').val(type);
        $('#editTitle').val(title);
        $('#editDescription').val(description);
        $('#editGenre').val(genre);
        $('#editReleaseYear').val(releaseYear);
        $('#editDirector').val(director);
    });
});