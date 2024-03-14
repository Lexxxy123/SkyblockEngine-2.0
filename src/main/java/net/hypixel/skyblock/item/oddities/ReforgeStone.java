/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Skyblock is licensed under the Creative Commons Non-Commercial 4.0 International License.
 *
 * You may not use this software for commercial use, however you are free
 * to modify, copy, redistribute, or build upon our codebase. You must give
 * appropriate credit, provide a link to the license, and indicate
 * if changes were made.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, visit https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */
package net.hypixel.skyblock.item.oddities;


import net.hypixel.skyblock.item.*;

public class ReforgeStone implements SkullStatistics, MaterialFunction, Reforgable {
    @Override
    public String getDisplayName()
    {
        return "Reforge Stone";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isStackable()
    {
        return false;
    }

    @Override
    public String getURL()
    {
        return "bdbe5a690633758683d3f1785541e6de481b775b3b1b552040da713b7738817c";
    }

    @Override
    public boolean displayRarity() {
        return false;
    }


}