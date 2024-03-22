package com.company.velogselectionuicomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.company.velogselectionuicomponent.ui.theme.VelogSelectionUiComponentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VelogSelectionUiComponentTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding()
                ) {
                    Checkboxes()
                }
            }
        }
    }
}

// 체크박스의 구성요소를 담을 데이터 클래스 선언 : 이 데이터 클래스의 객체를 체크 박스 한개라고 생각하면 된다.
data class CheckInfo(
    val isChecked: Boolean,
    val text: String
)

@Composable
fun Checkboxes() {
    // remember 함수는 컴포저블이 다시 호출 될 때(리컴포지션) 상태를 기억하게 해준다.
    // 따라서 mutableStateListOf()를 활용해서 변경 가능한 리스트를 생성해서 컴포즈가 리스트의 요소들을 추적할 수 있게
    // 해준다. 또한 위에서 생성한 데이터 클래스의 객체를 만들어서 체크박스 객체를 생성한 듯한 효과를 만들어준다.
    val checkboxes = remember {
        mutableStateListOf(
            CheckInfo(
                isChecked = false,
                text = "1"
            ),
            CheckInfo(
                isChecked = false,
                text = "2"
            ),
            CheckInfo(
                isChecked = false,
                text = "3"
            ),
        )
    }

    // allState에 ToggleableState.Indeterminate 를 초기값으로 넣어준다.
    // Toggle이란 두 가지 상태 사이를 왔다 갔다 하는 것을 의미한다.
    // ToggleableState는 enum class로 변수의 상태를 상수로 정의하였다. : On(활성화) Off(비활성화) Indeterminate(어중간)
    var allState by remember {
        mutableStateOf(ToggleableState.Indeterminate)
    }

    // goggleTriState라는 람다함수를 선언하였다.

    val toggleTriState = {
        allState = when (allState) {
            ToggleableState.Indeterminate -> ToggleableState.On
            ToggleableState.On -> ToggleableState.Off
            else -> ToggleableState.On
        }
        // indices는 컬렉션의 인덱스 범위를 반환하는 함수이다.
        // 따라서 indices.ferEach{}를 사용하면 , 반환된 인덱스 범위의 처음부터 끝까지 순회하면서
        // {} 블락 내부의 코드를 실행한다.
        checkboxes.indices.forEach { index ->
            // 만약 .copy()를 사용해서 기존의 checkbox의 isChecked 값만 변경된 새로운 객체를 반환한다.
            checkboxes[index] = checkboxes[index].copy(
                isChecked = allState == ToggleableState.On
            )
        }
    }

    // Row를 클릭해도 toggleTriState()가 실행되고 , TriStateCheckbox를 클릭해도 toggleTriState()가 실행된다.
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                toggleTriState()
            }
            .padding(end = 16.dp)
    ) {
        TriStateCheckbox(
            state = allState,
            onClick = toggleTriState
        )
        Text("전체 클릭")
    }

    // index는 checkboxes의 index 번호를 의미한다.
    // info는 checkboxes 객체 하나 하나에 대한 정보를 의미한다.
    checkboxes.forEachIndexed { index, info ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 32.dp)
                .clickable {
                    checkboxes[index] = info.copy(
                        isChecked = !info.isChecked
                    )
                }
                .padding(end = 16.dp)

        ) {
            Checkbox(
                checked = info.isChecked,
                onCheckedChange = { isChecked ->
                    checkboxes[index] = info.copy(
                        isChecked = isChecked
                    )

                })
            Text(text = info.text)
        }
    }

}
