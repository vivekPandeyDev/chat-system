-- Messages for Engineering Team (room 11111111-1111-1111-1111-111111111001)
INSERT INTO message (message_id, room_id, sender_id, content, type, sent_at, status) VALUES
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111001', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'Hello team, welcome!', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111001', 'a1b2c3d4-e5f6-11ee-0000-000000000009', 'Hi, ready for the sprint planning.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111001', 'a1b2c3d4-e5f6-11ee-0000-000000000021', 'Good morning everyone!', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111001', 'a1b2c3d4-e5f6-11ee-0000-000000000022', 'Hello!', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111001', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'Any updates on the project?', 'TEXT', NOW(), 'SENT');

-- Messages for QA Discussion Room (room 11111111-1111-1111-1111-111111111002)
INSERT INTO message (message_id, room_id, sender_id, content, type, sent_at, status) VALUES
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111002', 'a1b2c3d4-e5f6-11ee-0000-000000000010', 'QA team, start testing the new module.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111002', 'a1b2c3d4-e5f6-11ee-0000-000000000023', 'Noted, will begin now.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111002', 'a1b2c3d4-e5f6-11ee-0000-000000000024', 'Please report any critical bugs.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111002', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'On it!', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111002', 'a1b2c3d4-e5f6-11ee-0000-000000000010', 'Found a minor issue, logging it.', 'TEXT', NOW(), 'SENT');

-- Messages for Product Managers (room 11111111-1111-1111-1111-111111111003)
INSERT INTO message (message_id, room_id, sender_id, content, type, sent_at, status) VALUES
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111003', 'a1b2c3d4-e5f6-11ee-0000-000000000011', 'Team, review the roadmap.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111003', 'a1b2c3d4-e5f6-11ee-0000-000000000025', 'Reviewed, looks good.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111003', 'a1b2c3d4-e5f6-11ee-0000-000000000026', 'Adding notes for Q4 planning.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111003', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'Thanks everyone!', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111003', 'a1b2c3d4-e5f6-11ee-0000-000000000011', 'Reminder: send weekly updates.', 'TEXT', NOW(), 'SENT');

-- Messages for Support Team (room 11111111-1111-1111-1111-111111111004)
INSERT INTO message (message_id, room_id, sender_id, content, type, sent_at, status) VALUES
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111004', 'a1b2c3d4-e5f6-11ee-0000-000000000012', 'Support team, new tickets incoming.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111004', 'a1b2c3d4-e5f6-11ee-0000-000000000027', 'Acknowledged.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111004', 'a1b2c3d4-e5f6-11ee-0000-000000000028', 'Working on high-priority tickets.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111004', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'I will handle escalations.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111004', 'a1b2c3d4-e5f6-11ee-0000-000000000012', 'Ticket resolved, updating system.', 'TEXT', NOW(), 'SENT');

-- Messages for Marketing Chat (room 11111111-1111-1111-1111-111111111005)
INSERT INTO message (message_id, room_id, sender_id, content, type, sent_at, status) VALUES
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111005', 'a1b2c3d4-e5f6-11ee-0000-000000000013', 'Marketing campaign ideas?', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111005', 'a1b2c3d4-e5f6-11ee-0000-000000000029', 'We can do a social media push.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111005', 'a1b2c3d4-e5f6-11ee-0000-000000000030', 'Adding email marketing plan.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111005', 'a1b2c3d4-e5f6-11ee-0000-000000000001', 'Great, let’s sync tomorrow.', 'TEXT', NOW(), 'SENT'),
  (gen_random_uuid(), '11111111-1111-1111-1111-111111111005', 'a1b2c3d4-e5f6-11ee-0000-000000000013', 'Noted.', 'TEXT', NOW(), 'SENT');
