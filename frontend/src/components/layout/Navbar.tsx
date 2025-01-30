'use client';

import {
  Container,
  Group,
  Button,
  Text,
  Menu,
  UnstyledButton,
  Avatar,
  Drawer,
  Stack,
  Burger,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import Link from 'next/link';
import { useAuth } from '@/hooks/useAuth';
import classes from './Navbar.module.css';

interface NavbarProps {
  isAuthenticated: boolean;
}

export function Navbar({ isAuthenticated }: Readonly<NavbarProps>) {
  const { logout } = useAuth();
  const [opened, { toggle, close }] = useDisclosure(false);

  const items = isAuthenticated ? (
    <Stack gap="xs">
      <Button
        component={Link}
        href="/dashboard"
        variant="light"
        onClick={close}
      >
        Dashboard
      </Button>
      <Button
        variant="light"
        color="red"
        onClick={() => {
          close();
          logout();
        }}
      >
        Logout
      </Button>
    </Stack>
  ) : (
    <Stack gap="xs">
      <Button component={Link} href="/login" variant="light" onClick={close}>
        Log in
      </Button>
      <Button component={Link} href="/register" variant="light" onClick={close}>
        Sign up
      </Button>
    </Stack>
  );

  return (
    <header className={classes.header}>
      <Container size="lg" className={classes.inner}>
        <Text
          fw={700}
          variant="gradient"
          gradient={{ from: 'cyan', to: 'indigo' }}
          component={Link}
          href="/"
        >
          QRCodeGenerator
        </Text>

        <Group gap="xs" className={classes.links}>
          {isAuthenticated ? (
            <Menu position="bottom-end" width={150}>
              <Menu.Target>
                <UnstyledButton>
                  <Avatar size="sm" radius="xl" />
                </UnstyledButton>
              </Menu.Target>
              <Menu.Dropdown>
                <Menu.Item component={Link} href="/dashboard">
                  Dashboard
                </Menu.Item>
                <Menu.Item onClick={logout} color="red">
                  Logout
                </Menu.Item>
              </Menu.Dropdown>
            </Menu>
          ) : (
            <>
              <Button
                component={Link}
                href="/login"
                variant="default"
                size="sm"
              >
                Log in
              </Button>
              <Button component={Link} href="/register" size="sm">
                Sign up
              </Button>
            </>
          )}
        </Group>

        <Burger opened={opened} onClick={toggle} className={classes.burger} />

        <Drawer
          opened={opened}
          onClose={close}
          position="right"
          size="xs"
          title=""
        >
          {items}
        </Drawer>
      </Container>
    </header>
  );
}
