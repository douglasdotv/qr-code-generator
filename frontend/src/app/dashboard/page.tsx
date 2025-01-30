'use client';

import { Container, Title, Paper, Group, Button } from '@mantine/core';
import { useAuth } from '@/hooks/useAuth';
import classes from './page.module.css';

export default function DashboardPage() {
  const { logout } = useAuth();

  return (
    <Container size="lg" py="xl">
      <Paper shadow="sm" radius="md" p="xl" withBorder>
        <Group justify="space-between" mb="xl">
          <Title order={2}>Dashboard</Title>
          <Button variant="light" color="red" onClick={logout}>
            Logout
          </Button>
        </Group>

        <Title order={3} mb="md" className={classes.welcome}>
          Welcome to your QR code workspace!
        </Title>
      </Paper>
    </Container>
  );
}
