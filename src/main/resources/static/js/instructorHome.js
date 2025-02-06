document.querySelectorAll('.view-btn').forEach(button => {
  button.addEventListener('click', () => {
    alert('View Details clicked!');
  });
});

document.querySelectorAll('.add-task-btn').forEach(button => {
  button.addEventListener('click', () => {
    alert('Add Task clicked!');
  });
});
