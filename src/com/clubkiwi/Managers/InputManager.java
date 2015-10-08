package com.clubkiwi.Managers;

import com.clubkiwi.Character.Kiwi;
import com.clubkiwi.ClubKiwi;
import com.clubkiwi.World.Dispenser;
import com.clubkiwi.World.Zone;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Mathew on 9/29/2015.
 * Keyboard hook better than keylistener on all components
 */
public class InputManager implements KeyEventDispatcher
{
    private ClubKiwi ck;

    public InputManager(ClubKiwi ck)
    {
        //Set instance
        this.ck = ck;

        //Take control
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }

    public boolean dispatchKeyEvent(KeyEvent e)
    {
        if(e.getID() == KeyEvent.KEY_PRESSED)
            keyPressed(e.getKeyCode());
        else if(e.getID() == KeyEvent.KEY_RELEASED)
            keyReleased(e.getKeyCode());

        return false;
    }

    private void keyPressed(int keycode)
    {
        if (ck.gui.isIngame())
        {
            //Either control the chat or the game
            if (ck.gui.getChatbox().isFocusOwner())
            {
                //Send the chat message
                if (keycode == KeyEvent.VK_ENTER)
                    ck.gui.actionPerformed(new ActionEvent(ck.gui.getChatsend(), 101, "Send"));
            }
            else
            {
                //Open inv
                if (keycode == KeyEvent.VK_E)
                {
                    //Toggle inv
                    ck.invMgr.setVisible(!ck.invMgr.isVisible());

                    //Remove all movement flags
                    ck.getLocalKiwi().clearMoveStates();
                }

                //debug shit
                if (keycode == KeyEvent.VK_INSERT)
                {
                    for (Kiwi k : ck.players)
                        System.out.println(k);

                    ck.invMgr.addItemToInventory(ck.items.get(1));
                    ck.invMgr.addItemToInventory(ck.items.get(1));
                    ck.invMgr.addItemToInventory(ck.items.get(3));
                }

                if(keycode == KeyEvent.VK_HOME)
                {
                    ck.gui.SwitchToRoom(ck.gui.room2);
                }

                if(keycode == KeyEvent.VK_END)
                {
                    ck.gui.SwitchToRoom(ck.gui.main);
                }

                //Keyboard either controls the inventory or the kiwi
                if(ck.invMgr.isVisible())
                {
                    if (keycode == KeyEvent.VK_W)
                        ck.invMgr.prevItem();
                    else if (keycode == KeyEvent.VK_S)
                        ck.invMgr.nextItem();
                    else if (keycode == KeyEvent.VK_ENTER)
                        ck.invMgr.useItem();
                }
                else
                {

                    //Open the chat
                    if (keycode == KeyEvent.VK_ENTER)
                    {
                        ck.gui.getChatbox().requestFocus();
                    }

                    //Kiwi controls
                    if (keycode == KeyEvent.VK_W && !ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Up))
                    {
                        ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Up);
                    } else if (keycode == KeyEvent.VK_A && !ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Left))
                    {
                        ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Left);
                    } else if (keycode == KeyEvent.VK_D && !ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Right))
                    {
                        ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Right);
                    } else if (keycode == KeyEvent.VK_S && !ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Down))
                    {
                        ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Down);
                    }

                    if(keycode == KeyEvent.VK_E)
                    {
                        if(ck.gui.getCurrentRoom().getCollidingItem() != null)
                        {
                            //Dispenser
                            if(ck.gui.getCurrentRoom().getCollidingItem() instanceof Dispenser)
                                ((Dispenser)(ck.gui.getCurrentRoom().getCollidingItem())).activate();

                            //Zone
                            if(ck.gui.getCurrentRoom().getCollidingItem() instanceof Zone)
                                ((Zone)(ck.gui.getCurrentRoom().getCollidingItem())).activate();
                        }
                    }
                }
            }
        }
        else
        {
            //Login screen password box
            if(keycode == KeyEvent.VK_ENTER && ck.gui.getPassword().isFocusOwner())
                ck.gui.actionPerformed(new ActionEvent(ck.gui.getPassword(), 100, "Login"));

        }
    }

    private void keyReleased(int keycode)
    {
        if(ck.gui.isIngame())
        {
            //Kiwi controls
            if (keycode == KeyEvent.VK_W && ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Up))
            {
                ck.getLocalKiwi().removeMovestate(Kiwi.MoveState.Up);
            }
            else if (keycode == KeyEvent.VK_A && ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Left))
            {
                ck.getLocalKiwi().removeMovestate(Kiwi.MoveState.Left);
            }
            else if (keycode == KeyEvent.VK_D && ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Right))
            {
                ck.getLocalKiwi().removeMovestate(Kiwi.MoveState.Right);
            }
            else if (keycode == KeyEvent.VK_S && ck.getLocalKiwi().hasMoveState(Kiwi.MoveState.Down))
            {
                ck.getLocalKiwi().removeMovestate(Kiwi.MoveState.Down);
            }
        }
    }
}
