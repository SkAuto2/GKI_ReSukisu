from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]


class WorkflowKpmPolicyTest(unittest.TestCase):
    def test_resukisu_latest_kpm_is_not_rejected_by_branch_gate(self):
        build_workflow = (ROOT / ".github/workflows/build.yml").read_text(encoding="utf-8")

        self.assertIn("Official / KernelSU 变体不支持 KPM", build_workflow)
        self.assertNotIn("ReSukiSU 仅 Stable(标准) / Custom(自定义) 支持 KPM", build_workflow)
        self.assertNotRegex(
            build_workflow,
            re.compile(
                r"inputs\.ksu_variant\s*}}\s*\"\s*=\s*\"ReSukiSU\"[\s\S]+?"
                r"inputs\.ksu_branch\s*}}\s*\"\s*=\s*\"Latest\(最新\)\""
            ),
        )

    def test_full_feature_matrix_keeps_kpm_for_resukisu_latest(self):
        matrix_workflow = (ROOT / ".github/workflows/kernel-full-feature-matrix.yml").read_text(
            encoding="utf-8"
        )

        self.assertNotIn(
            "inputs.kernelsu_variant == 'ReSukiSU' && "
            "(inputs.kernelsu_branch == 'Stable(标准)' || "
            "inputs.kernelsu_branch == 'Custom(自定义)')",
            matrix_workflow,
        )
        self.assertIn(
            "inputs.use_kpm && "
            "(inputs.kernelsu_variant == 'SukiSU' || inputs.kernelsu_variant == 'ReSukiSU')",
            matrix_workflow,
        )


if __name__ == "__main__":
    unittest.main()
