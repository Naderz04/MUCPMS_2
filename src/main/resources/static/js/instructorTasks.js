 function createNewTask() {
        alert("New task creation functionality not implemented.");
    }

    function openDetailView(title, description, completedCount, totalProjects, dueDate) {
        document.getElementById('detailTitle').innerText = title;
        document.getElementById('detailDescription').innerText = description;
        document.getElementById('completedCount').innerText = completedCount;
        document.getElementById('totalProjects').innerText = totalProjects;
        document.getElementById('dueDate').innerText = dueDate;
        document.getElementById('detailView').style.display = 'block';
    }

    function closeDetailView() {
        document.getElementById('detailView').style.display = 'none';
    }

    function completeTask() {
        alert("Task marked as complete.");
        closeDetailView();
    }

    function deleteTask() {
        alert("Task deleted.");
        closeDetailView();
    }
   const modalOverlay = document.getElementById('modalOverlay');
        const taskForm = document.getElementById('taskForm');

        function openModal() {
            modalOverlay.style.display = 'flex';
        }

        function closeModal() {
            modalOverlay.style.display = 'none';
        }

        // Close modal when clicking outside
        modalOverlay.addEventListener('click', (e) => {
            if (e.target === modalOverlay) {
                closeModal();
            }
        });

        // Handle form submission
        taskForm.addEventListener('submit', (e) => {
            e.preventDefault();
            // Add your form handling logic here
            console.log({
                title: document.getElementById('title').value,
                description: document.getElementById('description').value,
                assignTo: document.getElementById('assignTo').value,
                color: document.getElementById('color').value,
                dueDate: document.getElementById('dueDate').value
            });
            closeModal();
        });