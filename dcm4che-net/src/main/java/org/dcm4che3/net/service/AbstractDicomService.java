/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che3.net.service;

import java.io.IOException;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Dimse;
import org.dcm4che3.net.PDVInputStream;
import org.dcm4che3.net.pdu.PresentationContext;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 *
 */
public abstract class AbstractDicomService implements DicomService {

    private final String[] sopClasses;

    protected AbstractDicomService(String... sopClasses) {
        this.sopClasses = sopClasses.clone();
    }

    @Override
    public String[] getSOPClasses() {
        return sopClasses;
    }

    @Override
    public void onClose(Association as) {
        // NOOP
    }

    @Override
    public void onDimseRQ(Association as, PresentationContext pc,
            Dimse dimse, Attributes cmd, PDVInputStream data) throws IOException {
        onDimseRQ(as, pc, dimse, cmd, readDataset(pc, data));
    }

    private Attributes readDataset(PresentationContext pc, PDVInputStream data)
            throws IOException {
        if (data == null)
            return null;

        Attributes dataset = data.readDataset(pc.getTransferSyntax());
        Dimse.LOG.debug("Dataset:\n{}", dataset);
        return dataset;
    }

    protected abstract void onDimseRQ(Association as, PresentationContext pc,
            Dimse dimse, Attributes cmd, Attributes data) throws IOException;


}
