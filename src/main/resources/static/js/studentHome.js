// Select all tabs and task sections
const tabs = document.querySelectorAll(".tab");
const taskSections = document.querySelectorAll(".task-section");

// Add click event to each tab
tabs.forEach(tab => {
  tab.addEventListener("click", () => {
    // Remove active class from all tabs
    tabs.forEach(t => t.classList.remove("active"));
    // Hide all task sections
    taskSections.forEach(section => section.classList.add("hidden"));

    // Set the clicked tab as active
    tab.classList.add("active");

    // Show the corresponding task section
    const sectionId = tab.dataset.section;
    document.getElementById(sectionId).classList.remove("hidden");
  });
});

// Add this to your existing JavaScript
document.querySelectorAll('.task').forEach(task => {
  task.addEventListener('click', (e) => {
    // Don't toggle if clicking buttons inside details
    if (e.target.closest('.add-work') || e.target.closest('.mark-done')) {
      return;
    }


    const details = task.querySelector('.details');
    const taskName = task.querySelector('h3').textContent;
    const dueDate = task.querySelector('p').textContent;

    // Update details content
    if (details) {
      details.querySelector('.task-name').textContent = taskName;
      details.querySelector('.due-date').textContent = dueDate;

      // Toggle details visibility
      details.classList.toggle('hidden');
    }
  });
});

// Prevent event bubbling for buttons
document.querySelectorAll('.add-work, .mark-done').forEach(button => {
  button.addEventListener('click', (e) => {
    e.stopPropagation();
    // Add your button click handlers here
  });
});
// Get references to elements
const fileInput = document.getElementById('fileInput');
const uploadedFilesContainer = document.getElementById('uploadedFiles');

// Handle file selection
fileInput.addEventListener('change', (e) => {
  const file = e.target.files[0];
  if (file) {
    // Show the uploaded files container
    uploadedFilesContainer.classList.remove('hidden');

    // Create uploaded file element
    const uploadedFile = document.createElement('div');
    uploadedFile.className = 'uploaded-file';

    // Format file size
    const fileSize = formatFileSize(file.size);

    uploadedFile.innerHTML = `
      <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Screenshot%20(7)-CdX9PJ0jE1Golg3tew7AYTpqO6natJ.png" alt="File" class="file-icon">
      <div class="file-info">
        <div class="file-name">${file.name}</div>
        <div class="file-size">${fileSize}</div>
      </div>
      <button class="remove-file" onclick="removeFile(this)">
        <span class="material-symbols-rounded">close</span>
      </button>
    `;

    // Add click handler to show full file
    uploadedFile.addEventListener('click', (e) => {
      if (!e.target.closest('.remove-file')) {
        // Here you would typically show a modal or preview of the file
        alert('Opening file: ' + file.name);
      }
    });

    // Clear existing files and add the new one
    uploadedFilesContainer.innerHTML = '';
    uploadedFilesContainer.appendChild(uploadedFile);
  }
});

// Helper function to format file size
function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// Remove file handler
function removeFile(button) {
  const fileElement = button.closest('.uploaded-file');
  fileElement.remove();
  fileInput.value = ''; // Clear the file input

  // Hide the container if no files
  if (uploadedFilesContainer.children.length === 0) {
    uploadedFilesContainer.classList.add('hidden');
  }
}

document.querySelectorAll('.add-m-btn').forEach(button => {
  button.addEventListener('click', () => {
    alert('Add Task clicked!');
  });
});




