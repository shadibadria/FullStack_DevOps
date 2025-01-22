const fs = require('fs');
const path = require('path');
const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const csv = require('fast-csv');

const app = express();
const GOALS_FILE = path.join(__dirname, 'goals.csv');

// Ensure goals.csv exists
if (!fs.existsSync(GOALS_FILE)) {
  fs.writeFileSync(GOALS_FILE, 'id,text\n', { flag: 'wx' }, (err) => {
    if (err && err.code !== 'EEXIST') {
      console.error('Error creating goals.csv:', err);
    }
  });
}

const accessLogStream = fs.createWriteStream(
  path.join(__dirname, 'logs', 'access.log'),
  { flags: 'a' }
);

app.use(morgan('combined', { stream: accessLogStream }));
app.use(bodyParser.json());

app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

// Read goals from CSV file
const readGoals = () => {
  return new Promise((resolve, reject) => {
    const goals = [];
    fs.createReadStream(GOALS_FILE)
      .pipe(csv.parse({ headers: true }))
      .on('data', (row) => goals.push(row))
      .on('end', () => resolve(goals))
      .on('error', (err) => reject(err));
  });
};

// Write goals to CSV file
const writeGoals = (goals) => {
  return new Promise((resolve, reject) => {
    const ws = fs.createWriteStream(GOALS_FILE);
    csv.write([['id', 'text'], ...goals.map((goal) => [goal.id, goal.text])], { headers: false })
      .pipe(ws)
      .on('finish', resolve)
      .on('error', reject);
  });
};

// Get all goals
app.get('/goals', async (req, res) => {
  try {
    const goals = await readGoals();
    res.status(200).json({ goals });
  } catch (err) {
    res.status(500).json({ message: 'Failed to load goals.', error: err.message });
  }
});

// Add a new goal
app.post('/goals', async (req, res) => {
  const goalText = req.body.text;
  if (!goalText || goalText.trim().length === 0) {
    return res.status(422).json({ message: 'Invalid goal text.' });
  }
  try {
    const goals = await readGoals();
    const newGoal = { id: Date.now().toString(), text: goalText };
    goals.push(newGoal);
    await writeGoals(goals);
    res.status(201).json({ message: 'Goal saved', goal: newGoal });
  } catch (err) {
    res.status(500).json({ message: 'Failed to save goal.', error: err.message });
  }
});

// Delete a goal
app.delete('/goals/:id', async (req, res) => {
  try {
    let goals = await readGoals();
    goals = goals.filter((goal) => goal.id !== req.params.id);
    await writeGoals(goals);
    res.status(200).json({ message: 'Deleted goal!' });
  } catch (err) {
    res.status(500).json({ message: 'Failed to delete goal.', error: err.message });
  }
});

// Start server
const PORT = process.env.PORT || 80;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
