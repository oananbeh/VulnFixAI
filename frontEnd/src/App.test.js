import { render, screen } from '@testing-library/react';
import App from './App';

test('renders VulnFixAI upload interface', () => {
  render(<App />);
  const titleElement = screen.getByText(/VulnFixAI: Upload Your Java Project/i);
  expect(titleElement).toBeInTheDocument();
});
